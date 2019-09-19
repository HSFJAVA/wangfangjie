<template>
  <div class="dashboard-container">
    <!-- <div><h2 style="align-content: center">欢迎您，<b>{{session}}</b></h2></div>
    <div class="dashboard-text">工号: {{Uid}}</div>
    <div class="dashboard-text">在职状态: {{statusstr}}</div>
    <div class="dashboard-text">职位: {{zhiweistr}}</div>
    <div class="dashboard-text">性别: {{sexstr}}</div>
    <div class="dashboard-text">联系方式: {{Uid}}</div>
    <div class="dashboard-text">地址: {{Uid}}</div>
    <div class="dashboard-text"></div> -->
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getsession } from "../../utils/pramAll";
export default {
  name: 'Dashboard',
      data() {
      return{
        session:""
      }
    },
  computed: {
    ...mapGetters([
      'name'
    ]),
  },
  mounted:function() {
    // this.session = sessionStorage.getItem("name");
    // this.Uid = sessionStorage.getItem("id");
    // this.statusstr = sessionStorage.getItem("status");
    // this.sexstr = sessionStorage.getItem("remark1");
    // this.zhiweistr = sessionStorage.getItem("remark2");
    console.log("123");
  }
    }
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
