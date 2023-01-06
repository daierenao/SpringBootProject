//存放蛇身体序列 蛇身体是一个圆 需要有坐标 及其中心
export class Cell {
  constructor(r, c) {
    this.r = r;
    this.c = c;
    //将r,c 转换为canvas坐标系
    this.x = c + 0.5;
    this.y = r + 0.5;
  }
}
