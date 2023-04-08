<template>
  <div style="background-color: rgb(199,237,204); width: 100%; height: 100vh; overflow: hidden">
    <div style="width: 400px; margin: 150px auto">
      <h1 style="text-align: center; color: limegreen">多任务协同管理平台</h1>
      <el-form ref="l_form" status-icon :model="l_form" :rules="rules" label-width="60px">
        <el-form-item style="text-align: center" prop="uid">
          <el-input prefix-icon="el-icon-user-solid" placeholder="ID" v-model="l_form.uid" clearable style="margin-top: 40px; margin-left: -60px"></el-input>
        </el-form-item>
        <el-form-item style="text-align: center" prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="密码" v-model="l_form.password" show-password style="margin-top: 5px; margin-left: -60px"></el-input>
        </el-form-item>
      </el-form>
      <el-row style="margin-top: 30px">
        <el-button type="success" plain style="width: 30%; margin-left: 30px" @click="register">注册</el-button>
        <el-button type="success" plain style="width: 30%; margin-left: 100px" @click="login()">登录</el-button>
      </el-row>
    </div>
  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: "Login",
  components: {

  },
  data() {
    //ID校验规则
    var validateID = (rule, value, callback) => {
      if(value === ''){
        callback(new Error('请输入ID'))
      } else if(value.length>10){
        callback(new Error('不能超过10位'));
      } else {
        callback();
      }
    };
    //密码校验规则
    var validatePass = (rule, value, callback) => {
      if(value === ''){
        callback(new Error('请输入密码'))
      } else if(value.length>10){
        callback(new Error('不能超过10位'));
      } else {
        callback();
      }
    };
    return {
      l_form: {
        uid: '',
        password: ''
      },
      rules: {
        uid: [
          {validator: validateID, trigger: 'blur'}
        ],
        password: [
          {validator: validatePass, trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    login(){
      this.$refs.l_form.validate((valid) => {
        if (valid) {
          request.post("/user/login",this.l_form).then(res => {
            if(res.code === '0'){
              this.$message({
                type: "success",
                message: "登录成功！"
              })
              sessionStorage.setItem("user",JSON.stringify(res.data))
              //页面跳转
              this.$router.push('/')
            }else{
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        } else {
          alert('请检查!!');
          return false;
        }
      })
    },
    register(){
      this.$router.push('/register')
    }
  }
}
</script>

<style scoped>

</style>