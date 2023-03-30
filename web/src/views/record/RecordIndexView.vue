<template>
  <ContentField>
    <table class="table table-striped table-hover" style="text-align: center">
      <thead>
        <tr>
          <th>A</th>
          <th>B</th>
          <th>游戏结果</th>
          <th>创建时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="record in records" :key="record.record.id">
          <td>
            <img :src="record.a_photo" alt="" />
            &nbsp;
            {{ record.a_username }}
          </td>
          <td>
            <img :src="record.b_photo" alt="" />
            &nbsp;
            {{ record.b_username }}
          </td>
          <td>{{ record.result }}</td>
          <td>{{ record.record.createtime }}</td>
          <td>
            <button
              @click="open_record(record.record)"
              type="button"
              class="btn btn-secondary"
            >
              查看录像
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <nav aria-label="Page navigation example" style="float: right">
      <ul class="pagination">
        <li @click="click_page_btn(-2)" class="page-item">
          <a class="page-link" href="####">前一页</a>
        </li>
        <li
          @click="click_page_btn(page.number)"
          :class="'page-item ' + page.is_active"
          v-for="page in pages"
          :key="page.number"
        >
          <a class="page-link" href="####">{{ page.number }}</a>
        </li>
        <li @click="click_page_btn(-1)" class="page-item">
          <a class="page-link" href="####">后一页</a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>

<script>
import { useStore } from "vuex";
import { ref } from "vue";
import $ from "jquery";
import ContentField from "../../components/ContentField";
import router from "../../router/index";
export default {
  components: {
    ContentField,
  },

  setup() {
    const store = useStore();
    let records = ref([]);
    let current_page = 1;
    let total_records = 0;
    let pages = ref([]);
    console.log(total_records);

    const click_page_btn = (page) => {
      if (page === -2) {
        page = current_page - 1;
      } else if (page === -1) {
        page = current_page + 1;
      }
      let max_pages = parseInt(Math.ceil(total_records / 10));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    };

    const update_page = () => {
      let max_pages = parseInt(Math.ceil(total_records / 10));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; i++) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? "active" : "",
          });
        }
      }
      pages.value = new_pages;
    };

    const pull_page = (page) => {
      current_page = page;
      $.ajax({
        url: "http://127.0.0.1:3000/record/getlist/",
        type: "get",
        data: {
          page,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },

        success(resp) {
          records.value = resp.records;
          total_records = resp.records_count;
          update_page();
          console.log(pages.value);
        },
        error(resp) {
          console.log(resp);
        },
      });
    };
    pull_page(current_page);

    const stringTo2D = (map) => {
      let g = [];
      for (let i = 0, k = 0; i < 13; i++) {
        let line = [];
        for (let j = 0; j < 14; j++, k++) {
          if (map[k] === "1") line.push(1);
          else line.push(0);
        }
        g.push(line);
      }
      return g;
    };

    const open_record = (record) => {
      console.log(record);
      console.log(store.state.record);
      console.log(store.state.pk);
      store.commit("updateIsRecord", true);
      store.commit("updateGameMap", {
        map: stringTo2D(record.map),
        a_id: record.aid,
        b_id: record.bid,
        a_sx: record.asx,
        a_sy: record.asy,
        b_sx: record.bsx,
        b_sy: record.bsy,
      });
      store.commit("updateSteps", {
        a_steps: record.asteps,
        b_steps: record.bsteps,
      });
      store.commit("updateRecordLoser", record.loser);
      router.push({
        name: "record_content",
        params: {
          recordId: record.id,
        },
      });
    };

    return {
      records,
      open_record,
      pages,
      click_page_btn,
    };
  },
};
</script>

<style scoped>
img {
  width: 6vh;
  border-radius: 50%;
}
</style>
