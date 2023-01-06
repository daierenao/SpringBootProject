import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";
import { Snake } from "./Snake";
export class GameMap extends AcGameObject {
  //ctx画布， parent用来动态修改画布的长宽
  constructor(ctx, parent) {
    super();
    this.ctx = ctx;
    this.parent = parent;
    //像素块的绝对距离 需要动态获取
    this.L = 0;
    this.rows = 13;
    this.cols = 14;
    this.walls = [];
    this.inner_walls = 30;
    this.snakes = [
      new Snake({ id: 0, color: "#4876EC", r: this.rows - 2, c: 1 }, this),
      new Snake({ id: 1, color: "#F94848", r: 1, c: this.cols - 2 }, this),
    ];
  }

  //判断起点和终点的连通性 只有连通地图才合法
  check_connectivity(g, sx, sy, ex, ey) {
    if (sx == ex && sy == ey) return true;
    //标记已经走过
    g[sx][sy] = true;

    let dx = [0, 1, 0, -1];
    let dy = [-1, 0, 1, 0];
    for (let i = 0; i < 4; i++) {
      let x = sx + dx[i],
        y = sy + dy[i];
      if (!g[x][y] && this.check_connectivity(g, x, y, ex, ey)) {
        return true;
      }
    }
    return false;
  }
  create_walls() {
    //用bool数组表示墙 true表示有墙， false 表示没墙
    const g = [];
    for (let r = 0; r < this.rows; r++) {
      g[r] = [];
      for (let c = 0; c < this.cols; c++) {
        g[r][c] = false;
      }
    }
    //周围的墙
    for (let r = 0; r < this.rows; r++) {
      g[r][0] = g[r][this.cols - 1] = true;
    }
    //周围的墙
    for (let c = 0; c < this.cols; c++) {
      g[0][c] = g[this.rows - 1][c] = true;
    }
    //随机生成内部的障碍物
    for (let i = 0; i < this.inner_walls / 2; i++) {
      for (let j = 0; j < 1000; j++) {
        //生成(0 ~ 13)随机行数
        let r = parseInt(Math.random() * this.rows);
        //生成随机列数
        let c = parseInt(Math.random() * this.cols);
        //如果该位置已经生成了障碍物则跳过
        if (g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) continue;
        //不能将两个玩家的起点设为障碍
        if ((r == this.rows - 2 && c == 1) || (c == this.cols - 2 && r == 1))
          continue;
        g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
        break;
      }
    }
    const copy_g = JSON.parse(JSON.stringify(g));
    if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2))
      return false;
    //生成障碍物
    for (let r = 0; r < this.rows; r++)
      for (let c = 0; c < this.cols; c++) {
        if (g[r][c]) {
          this.walls.push(new Wall(r, c, this));
        }
      }
    return true;
  }
  //绑定键盘输入事件
  add_listening_event() {
    this.ctx.canvas.focus();
    const [snake1, snake2] = this.snakes;
    //根据键盘输入 设置两条蛇的移动方向
    this.ctx.canvas.addEventListener("keydown", (e) => {
      if (e.key === "w") snake1.set_direction(0);
      else if (e.key === "d") snake1.set_direction(1);
      else if (e.key === "s") snake1.set_direction(2);
      else if (e.key === "a") snake1.set_direction(3);
      else if (e.key === "ArrowUp") snake2.set_direction(0);
      else if (e.key === "ArrowRight") snake2.set_direction(1);
      else if (e.key === "ArrowDown") snake2.set_direction(2);
      else if (e.key === "ArrowLeft") snake2.set_direction(3);
    });
  }
  start() {
    for (let i = 0; i < 100; i++) if (this.create_walls()) break;

    this.add_listening_event();
  }
  update_size() {
    this.L = parseInt(
      Math.min(
        this.parent.clientWidth / this.cols,
        this.parent.clientHeight / this.rows
      )
    );
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }
  //判断两条蛇是否都准备好下一个回合
  check_ready() {
    //遍历两条蛇的移动状态
    for (const snake of this.snakes) {
      if (snake.status !== "idle") return false;
      if (snake.directions === -1) return false;
    }
    return true;
  }
  check_valid(cell) {
    //判断是否撞墙
    for (const wall of this.walls) {
      if (cell.r == wall.r && cell.c == wall.c) return false;
    }
    //判断是否撞蛇
    for (const snake of this.snakes) {
      let k = snake.cells.length;
      //对于蛇头撞蛇尾的情况，如果蛇尾增长则会撞上 如果蛇尾不增长 则删去移动前蛇尾的判断
      if (!snake.check_tail_increasing) k--;
      for (let i = 0; i < k; i++) {
        if (snake.cells[i].r == cell.r && snake.cells[i].c == cell.c)
          return false;
      }
    }
    return true;
  }
  next_step() {
    //两条蛇进入下一回合
    for (const snake of this.snakes) {
      snake.next_step();
    }
  }
  update() {
    this.update_size();
    if (this.check_ready()) {
      //如果两条蛇都准备好移动 则更新状态
      this.next_step();
    }
    //每一帧执行一次
    this.render();
  }
  //渲染画布
  render() {
    const color_even = "#AAD751";
    const color_odd = "#A2D149";
    for (let r = 0; r < this.rows; r++)
      for (let c = 0; c < this.cols; c++) {
        if ((r + c) % 2 == 0) {
          this.ctx.fillStyle = color_odd;
        } else this.ctx.fillStyle = color_even;
        this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
      }
  }
}
