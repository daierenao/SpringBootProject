//蛇

import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";
export class Snake extends AcGameObject {
  //通过json数据info传入蛇对应的玩家，颜色，和画蛇需要的工具。
  constructor(info, gamemap) {
    super();
    this.id = info.id;
    this.color = info.color;
    this.gamemap = gamemap;
    this.speed = 5; //每秒移动五个格子
    this.directions = -1; //-1表示没有指令，0、1、2、3表示上右下左四个方向
    this.status = "idle"; //idle表示静止，move表示移动，die表示死亡
    //蛇的身体
    this.cells = [new Cell(info.r, info.c)]; //存放蛇的身体 cells[0]存放蛇头
    this.next_cell = null; // 下一步的目标位置。
    this.dr = [-1, 0, 1, 0]; //行方向偏移量
    this.dc = [0, 1, 0, -1]; //列方向偏移量
    this.step = 0; //回合数
    this.eps = 1e-2; //允许的误差
    this.eye_direction = 0;
    if (this.id === 1) this.eye_direction = 2; //如果玩家在右上角则眼睛方向向下
    //两只眼睛的方向 相对于中心点
    this.eye_dx = [
      //第一个左眼 ， 第二个右眼
      [-1, 1],
      [1, 1],
      [1, -1],
      [-1, -1],
    ];
    this.eye_dy = [
      [-1, -1],
      [-1, 1],
      [1, 1],
      [1, -1],
    ];
  }
  start() {}
  set_direction(d) {
    this.directions = d;
  }

  //判断是否增长
  check_tail_increasing() {
    if (this.step <= 10) return true;
    if (this.step % 3 === 1) return true;
    return false;
  }
  //将状态更新到下一回合
  next_step() {
    //每次移动创建一个虚拟的蛇头，该蛇头移动后，将原蛇头移动到该蛇头上
    const d = this.directions;

    this.next_cell = new Cell(
      this.cells[0].r + this.dr[d],
      this.cells[0].c + this.dc[d]
    );
    //更新眼睛状态
    this.eye_direction = d;
    this.directions = -1; //清空状态
    this.status = "move";
    this.step++;
    //除了蛇头的中间部分都移动到前面的身体上，这样每移动一次就多了一个蛇序列。
    const k = this.cells.length;
    for (let i = k; i > 0; i--) {
      this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1])); //避免重复引用
    }

    // if (!this.gamemap.check_valid(this.next_cell)) {
    //   //如果下一步撞墙或撞到蛇 则蛇死亡
    //   this.status = "die";
    // }
  }
  update_move() {
    //移动距离等于速度乘以时间 ， 这里的时间是两帧之间的间隔 单位是毫秒

    const dx = this.next_cell.x - this.cells[0].x;
    const dy = this.next_cell.y - this.cells[0].y;
    const distance = Math.sqrt(dx * dx + dy * dy);
    //当目标点和移动后的点重合则停止移动 允许误差0.01
    if (distance < this.eps) {
      this.cells[0] = this.next_cell;
      this.next_cell = null;
      this.status = "idle";

      if (!this.check_tail_increasing()) {
        //蛇不变长 删除蛇尾
        this.cells.pop();
      }
    }
    //如果还没移动到目标位置 则移动蛇头
    else {
      const move_distance = (this.speed * this.timedelta) / 1000;
      this.cells[0].x += (move_distance * dx) / distance;
      this.cells[0].y += (move_distance * dy) / distance;
      //如果增长 则不移动队尾 如果不增长则需要移动队尾 移动后需要删除队尾
      if (!this.check_tail_increasing()) {
        //将蛇尾移动到前一个的位置
        const k = this.cells.length;
        const tail = this.cells[k - 1],
          tail_target = this.cells[k - 2];
        const tail_dx = tail_target.x - tail.x;
        const tail_dy = tail_target.y - tail.y;
        tail.x += (move_distance * tail_dx) / distance;
        tail.y += (move_distance * tail_dy) / distance;
      }
    }
  }
  update() {
    //每一帧执行一次 状态更新后才会移动
    if (this.status === "move") this.update_move();
    this.render();
  }
  render() {
    const L = this.gamemap.L;
    const ctx = this.gamemap.ctx;
    ctx.fillStyle = this.color;
    if (this.status === "die") ctx.fillStyle = "white";
    for (const cell of this.cells) {
      ctx.beginPath();
      //arc传入圆的中心坐标，圆的半径，最后两个参数是圆弧的范围整个圆即0~2Π
      ctx.arc(cell.x * L, cell.y * L, (L / 2) * 0.8, 0, Math.PI * 2);
      ctx.fill();
    }
    for (let i = 1; i < this.cells.length; i++) {
      const a = this.cells[i - 1],
        b = this.cells[i];
      //如果两个相邻的圆上下分布
      if (Math.abs(a.x - b.x) < this.eps) {
        ctx.fillRect(
          (a.x - 0.4) * L, //左上角x坐标
          Math.min(a.y, b.y) * L, //左上角y坐标
          L * 0.8, //画的长度
          Math.abs(a.y - b.y) * L //画的宽度
        );
        //如果两个相邻的圆左右分布
      } else {
        ctx.fillRect(
          Math.min(a.x, b.x) * L, //左上角x坐标
          (a.y - 0.4) * L, //左上角y坐标
          Math.abs(a.x - b.x) * L, //画的长度
          L * 0.8 //画的宽度
        );
      }
    }
    ctx.fillStyle = "black";
    for (let i = 0; i < 2; i++) {
      ctx.beginPath();
      const eye_x =
        (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
      const eye_y =
        (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;
      ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
      ctx.fill();
    }
  }
}
