const AC_GAME_OBJECTS = [];

export class AcGameObject {
  constructor() {
    AC_GAME_OBJECTS.push(this);
    this.timedelta = 0;
    this.has_called_start = false;
  }
  //只执行一次
  start() {}
  //除第一帧外，每一帧都执行
  update() {}
  //结束之前执行
  on_destory() {}
  //结束时执行
  destory() {
    this.on_destory();
    for (let i in AC_GAME_OBJECTS) {
      const obj = AC_GAME_OBJECTS[i];
      if (obj === this) {
        AC_GAME_OBJECTS.splice(i);
        break;
      }
    }
  }
}
let last_timestamp;
const step = (timestamp) => {
  //每一帧都刷新所有游戏对象
  for (let obj of AC_GAME_OBJECTS) {
    if (!obj.has_called_start) {
      obj.has_called_start = true;
      obj.start();
    } else {
      obj.timedelta = timestamp - last_timestamp;
      obj.update();
    }
  }
  last_timestamp = timestamp;
  requestAnimationFrame(step);
};

requestAnimationFrame(step);
