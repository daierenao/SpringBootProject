<template>
  <PlayGround v-if="$store.state.pk.status === 'playing'"> </PlayGround>
  <MatchGround v-if="$store.state.pk.status === 'matching'"> </MatchGround>
  <ResultBoard v-if="$store.state.pk.loser != 'none'"></ResultBoard>
</template>

<script>
import PlayGround from "../../components/PlayGround";
import MatchGround from "../../components/MatchGround";
import ResultBoard from "../../components/ResultBoard.vue";
import { onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";

export default {
  components: {
    PlayGround,
    MatchGround,
    ResultBoard,
  },
  setup() {
    //键盘映射
    const direction_mapping = (d) => {
      if (d == 0) d = 2;
      else if (d == 1) d = 3;
      else if (d == 2) d = 0;
      else if (d == 3) d = 1;
      return d;
    };
    const store = useStore();
    let socket = null;
    const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}`;
    onMounted(() => {
      store.commit("updateOpponent", {
        username: "我的对手",
        photo:
          "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
      });
      socket = new WebSocket(socketUrl);

      socket.onopen = () => {
        console.log("connected");
        store.commit("updateSocket", socket);
      };

      socket.onclose = () => {
        console.log("disconnected");
      };

      socket.onmessage = (msg) => {
        const data = JSON.parse(msg.data);
        if (data.event === "start-matching") {
          store.commit("updateOpponent", {
            username: data.opponent_username,
            photo: data.opponent_photo,
          });
          setTimeout(() => {
            store.commit("updateStatus", "playing");
          }, 300);
          store.commit("updateGameMap", data.game);
        } else if (data.event === "move") {
          const gameObject = store.state.pk.gameObject;
          const [snake0, snake1] = gameObject.snakes;
          if (store.state.user.id == store.state.pk.a_id) {
            snake0.set_direction(data.a_direction);
            snake1.set_direction(data.b_direction);
          } else if (store.state.user.id == store.state.pk.b_id) {
            snake0.set_direction(direction_mapping(data.b_direction));
            snake1.set_direction(direction_mapping(data.a_direction));
          }
        } else if (data.event === "result") {
          const gameObject = store.state.pk.gameObject;
          const [snake0, snake1] = gameObject.snakes;
          //设置临死前 眼睛的方向
          if (store.state.user.id == store.state.pk.a_id) {
            if (data.a_direction != undefined)
              snake0.eye_direction = data.a_direction;
            if (data.b_direction != undefined)
              snake1.eye_direction = data.b_direction;
          } else if (store.state.user.id == store.state.pk.b_id) {
            if (data.b_direction != undefined)
              snake0.eye_direction = direction_mapping(data.b_direction);
            if (data.a_direction != undefined)
              snake1.eye_direction = direction_mapping(data.a_direction);
          }
          if (data.loser === "all" || data.loser === "A") {
            if (store.state.user.id == store.state.pk.a_id)
              snake0.status = "die";
            else if (store.state.user.id == store.state.pk.b_id)
              snake1.status = "die";
          }
          if (data.loser === "all" || data.loser === "B") {
            if (store.state.user.id == store.state.pk.a_id)
              snake1.status = "die";
            else if (store.state.user.id == store.state.pk.b_id)
              snake0.status = "die";
          }
          store.commit("updateLoser", data.loser);
        }
      };
    });
    onUnmounted(() => {
      socket.close();
      store.commit("updateStatus", "matching");
    });
  },
};
</script>

<style scoped></style>
