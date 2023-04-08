<template>
  <div style="background-color: rgb(199,237,204); width: 100%; height: 100vh; overflow: hidden">
    <div style="width: 400px; margin: 100px auto">
      <h1 style="text-align: center; color: limegreen">多任务协同管理平台</h1>
      <el-form ref="r_form" status-icon :model="r_form" :rules="rules" label-width="60px">
        <el-form-item style="text-align: center" prop="name">
          <el-input placeholder="姓名" v-model="r_form.name" clearable style="margin-top: 40px; margin-left: -60px"></el-input>
        </el-form-item>
        <el-form-item style="text-align: center" prop="uid">
          <el-input prefix-icon="el-icon-user-solid" placeholder="设置ID" v-model="r_form.uid" clearable style="margin-top: 5px; margin-left: -60px"></el-input>
        </el-form-item>
        <el-form-item style="text-align: center" prop="company">
          <el-input placeholder="单位名称" v-model="r_form.company" clearable style="margin-top: 5px; margin-left: -60px"></el-input>
        </el-form-item>
        <el-form-item style="text-align: center" prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="设置密码" v-model="r_form.password" show-password style="margin-top: 5px; margin-left: -60px"></el-input>
        </el-form-item>
        <el-form-item style="text-align: center" prop="verifyp">
          <el-input prefix-icon="el-icon-lock" placeholder="确认密码" v-model="r_form.verifyp" show-password style="margin-top: 5px; margin-left: -60px"></el-input>
        </el-form-item>
      </el-form>
      <el-row style="margin-top: 30px">
        <el-button type="success" plain style="width: 85%; margin-left: 30px" @click="register()">注册</el-button>
      </el-row>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Register",
  data() {
    //ID不能为空
    var validateID = (rule, value, callback) => {
      if(value === ''){
        callback(new Error('请设置ID'))
      } else if(value.length>10){
        callback(new Error('不能超过10位'));
      } else {
        callback();
      }
    }
    //姓名、单位校验规则
    var validateLen = (rule, value, callback) => {
      if(value.length>10){
        callback(new Error('不能超过10位'));
      } else {
        callback();
      }
    };
    //密码校验规则
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请设置密码'));
      } else if(value.length>20){
        callback(new Error('不能超过20位'));
      } else {
        if (this.r_form.verifyp !== '') {
          this.$refs.r_form.validateField('verifyp');
        }
        callback();
      }
    };
    //确认密码校验规则
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.r_form.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      r_form: {
        name: '',
        uid: '',
        company: '',
        password: '',
        verifyp: ''
      },
      rules: {
        name: [
          {validator: validateLen, trigger: 'blur'}
        ],
        uid: [
          {validator: validateID, trigger: 'blur'}
        ],
        company: [
          {validator: validateLen, trigger: 'blur'}
        ],
        password: [
          {validator: validatePass, trigger: 'blur'}
        ],
        verifyp: [
          {validator: validatePass2, trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    register(){
      this.$refs.r_form.validate((valid) => {
        if (valid) {
          request.post("/user/register",this.r_form).then(res => {
            if(res.code === '0'){
              this.$message({
                type: "success",
                message: "注册成功！"
              })
              this.$router.push('/login')
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
      });
    },
  }
}
</script>

<style scoped>

</style>