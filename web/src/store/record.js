export default {
  state: {
    is_record: false, //由于共用GameMap 设置is_record表示正在播放录像 而不是进行游戏
    a_steps: "",
    b_steps: "",
    record_loser: "",
  },
  getters: {},
  mutations: {
    updateIsRecord(state, record) {
      state.is_record = record;
    },
    updateSteps(state, data) {
      state.a_steps = data.a_steps;
      state.b_steps = data.b_steps;
    },
    updateRecordLoser(state, loser) {
      state.record_loser = loser;
    },
  },
  actions: {},
  modules: {},
};
