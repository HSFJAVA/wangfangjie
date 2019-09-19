<template>
<el-container class="contailerMain">
      <el-header style="text-align: right; font-size: 12px; background-color:#3c8dbc">
       <div style="height:60px; float:left; padding-left:25px">
            <img style="height:60px" src="../assets/worldChina.png"></img>
        </div>
        <el-dropdown @command="handleCommand">
          <span  class="el-dropdown-link">
            {{userName }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="">退出登陆</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <!-- <span style="color:#fff">{{userName }}</span> -->
        <div style="height:60px; float:right; margin-left:50px">
            <img style="height:50px; margin-top:5px" src="../assets/sandao.png"></img>
      </div>
      </el-header>
    <el-container style="height: 100%; border: 1px solid #eee">
    <el-aside width="200px" style="background-color: #222d32">
      <el-menu :default-openeds="['1', '3']" router  background-color="#222d32"
      text-color="#fff"
      active-text-color="#ffd04b">
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-tickets"></i>业务操作
          </template>
          <el-menu-item-group>
            <!-- <el-menu-item index="/exportenter">进仓数据录入</el-menu-item>
            <el-menu-item index="/importenter">出仓数据录入</el-menu-item> -->
            <el-menu-item index="/YewuList">业务数据列表</el-menu-item>
            <el-menu-item index="/OcrBaseData">识别数据展示</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-menu"></i>导航二
          </template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="2-1">选项1</el-menu-item>
            <el-menu-item index="2-2">选项2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组2">
            <el-menu-item index="2-3">选项3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="2-4">
            <template slot="title">选项4</template>
            <el-menu-item index="2-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-main >
        <router-view/>
      </el-main>
      <el-footer style="text-align:center"></el-footer>
    </el-container>
  </el-container>
  </el-container>

</template>

<script>
export default {
  data() {
    const item = {
      date: "2016-05-02",
      name: "测试人员",
      address: "上海市普陀区金沙江路 1518 弄"
    };
    return {
      isCollapse: false,
      tableData: Array(20).fill(item),
      userName:""
    };
  },
  mounted:function() {
    this.getsession();
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
      getsession() {
      console.log(44444);
      console.log(sessionStorage.getItem("name"));
      this.userName = sessionStorage.getItem("name");
  },
  handleCommand() {
    console.log(4444);
    sessionStorage.removeItem("name");
    this.$router.push('/');
  }
  }
};
</script>

<style>
*{
  margin:0;
  padding: 0;
}
html, body{
    height: 100%;
}
.contailerMain {
  height:100%;
}
.el-header {
  background-color: #b3c0d1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
</style>
