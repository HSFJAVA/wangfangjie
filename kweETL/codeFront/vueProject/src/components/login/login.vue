<template>
    
    <div >
        <div class="loginBox">
            <div class="loginPng">
                <img class="imgBack" src="../../assets/u1.png" alt="">
            </div>
            <div class="loginInput">
                <el-form  status-icon :model="ruleForm"  class="demo-ruleForm" label-width="80px">
                    <el-form-item label="用户名：" >
                        <el-input type="name"  autocomplete="off" v-model="ruleForm.name" placeholder="请输入用户名"></el-input>
                    </el-form-item>
                    <el-form-item  label="密码">
                        <el-input type="password" autocomplete="off" v-model="ruleForm.password" placeholder="请输入密码"></el-input>
                    </el-form-item>
                    <div class="Autologon">
                        <span>
                            <input type="checkbox">
                            自动登录
                        </span>
                        <span>
                            <a href="#" @click="passwordback">密码找回</a>
                        </span>
                    </div>
                    <el-form-item>
                        <el-button class="loginBtn" type="info" @click="submitForm('ruleForm2')">登录</el-button>
                    </el-form-item>
                    </el-form>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name:"login",
    data() {
        return {
          ruleForm: {
          name: '',
          password: ''
        }
        }
    },
    methods: {
        passwordback () {
          this.$message({
          message: '请联系管理员进行修改',
          type: 'warning'
          });
        },
        submitForm () {
          console.warn(1)
          console.log(this.ruleForm);
          const _this = this;
         this.$http
              .get(`/apis/kwe/index/login?userName=${this.ruleForm.name}&passwd=${this.ruleForm.password}`)
              .then(function(resp) {
                if(resp.data == "error") {
                              _this.$notify.error({
                                title: '错误',
                                message: '请输入正确的用户名密码'
                                });
                } else {
                     _this.$router.push('/YewuList');
                     sessionStorage.setItem("name", _this.ruleForm.name);
                     console.log(sessionStorage.getItem("name"))
                }
              });
        
    }
    }
}
</script>

<style>
    .loginBox{
        height: 500px;
        width: 400px;
        position: absolute;
        left:50%;
        top:50%; 
        margin-left:-200px;
        margin-top:-250px;
    }
    .loginPng {
        width: 100%;
        text-align: center;
        margin-bottom: 20px;
    }
    .Autologon{
        text-align: right;
        margin-bottom: 10px;
    }
    .imgBack{
       width: 180px;
       height: 130px;
       margin-left: 100px
    }
    .loginBtn{
        width: 100%;
    }
</style>

