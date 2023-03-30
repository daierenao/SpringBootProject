<template>
  <div class="result-board">
    <div class="result-board-text" v-if="$store.state.pk.loser === 'all'">
      Draw
    </div>
    <div
      class="result-board-text"
      v-else-if="
        $store.state.pk.loser === 'A' &&
        $store.state.pk.a_id == $store.state.user.id
      "
    >
      <div>Lose</div>
      <div class="result-board-text-rating">
        排名:{{ $store.state.user.rating }}(-2)
      </div>
    </div>
    <div
      class="result-board-text"
      v-else-if="
        $store.state.pk.loser === 'B' &&
        $store.state.pk.b_id == $store.state.user.id
      "
    >
      <div>Lose</div>
      <div class="result-board-text-rating">
        排名:{{ $store.state.user.rating }}(-2)
      </div>
    </div>
    <div class="result-board-text" v-else>
      <div>Win</div>
      <div class="result-board-text-rating">
        排名:{{ $store.state.user.rating }}(+5)
      </div>
    </div>
    <div class="result-board-btn">
      <button @click="restart" type="button" class="btn btn-warning">
        再来一局！
      </button>
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";

export default {
  setup() {
    const store = useStore();
    const restart = () => {
      store.commit("updateStatus", "matching");
      store.commit("updateOpponent", {
        username: "我的对手",
        photo:
          "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
      });
      store.commit("updateLoser", "none");
    };
    return {
      restart,
    };
  },
};
</script>

<style scoped>
.result-board {
  height: 45vh;
  width: 30vw;
  background-color: rgba(50, 50, 50, 0.5);
  position: absolute;
  top: 30vh;
  left: 35vw;
}
.result-board-text {
  text-align: center;
  font-size: 50px;
  color: white;
  font-style: italic;
  font-weight: 600;
  padding-top: 5vh;
}
.result-board-text-rating {
  font-size: 30px;
  padding-top: 3vh;
}
.result-board-btn {
  padding-top: 6vh;
  text-align: center;
}
</style>
