<template>
  <div>
    <Header/>
    <div style="height: 10px"></div>
    <div style="float: left; height: 627px; width: 450px; margin-left: 10px; background-color: floralwhite">
      <div style="margin-left: 30px; margin-top: 40px; font-size: large">
        任务名称：{{taskData.title}}
        <br/>
        <br/>
        <br/>
        任务简介：{{taskData.intro}}
        <br/>
        <br/>
        <br/>
        创建时间：{{taskData.startdate}}
        <br/>
        <br/>
        <br/>
        截止时间：{{taskData.ddl}}
        <br/>
        <br/>
        <br/>
        状态：{{taskData.done}}
        <br/>
        <br/>
        <br/>
        类型：
        <span v-if="taskData.coop===0">个人任务</span>
        <span v-if="taskData.coop===1">项目任务</span>
        <br/>
        <br/>
        <br/>
        重要程度：
        <span v-if="taskData.emer===0">非常重要</span>
        <span v-if="taskData.emer===1">重要</span>
        <span v-if="taskData.emer===2">普通</span>
        <span v-if="taskData.emer===3">不重要</span>
        <div v-if="taskData.coop===1">
          <br/>
          <br/>
          所属项目：<a class="link" @click="gotoProj">{{projectData.title}}</a>
        </div>
        <div v-if="taskData.coop===0">
          <br/>
          <br/>
          操作：
          <el-button @click="doneTask" size="medium" type="success" style="margin-left: 10px;">完成任务</el-button>
          <el-button @click="endTask" size="medium" type="success" style="margin-left: 20px;">终止任务</el-button>
        </div>
      </div>
    </div>
    <!--    右边-->
    <div style="float: right; height: 627px; width: 1050px; margin-right: 10px; background-color: white">
      <div v-if="this.user.uid!==this.taskData.uid&&this.groupData.resp!==1" style="color: #606266; font-size: 30px; text-align: center; margin-top: 250px">您没有查看权限！</div>
      <div v-else>
        <el-upload
            class="upload-demo"
            drag
            :action="'/api/file/upload/' + this.taskId"
            style="margin-left: 330px; margin-top: 120px"
            multiple>
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
        <div style="margin-top: 20px; text-align: center"><a class="a" @click="gotoFileList">查看已上传的文件列表</a></div>
        <div v-if="this.taskData.coop === 1 && this.user.uid === this.taskData.uid" style="margin-top: 50px; margin-left: 480px"><el-button @click="submitTask" size="medium" type="success">提交任务</el-button></div>
        <div style="margin-top: 40px; text-align: center"><a class="a" @click="gotoComment">查看审批记录</a></div>
        <div v-if="this.taskData.coop === 1 && this.groupData.resp === 1" style="margin-top: 40px; margin-left: 410px">项目负责人操作： <el-button @click="approve" size="medium" type="success">审批任务</el-button></div>
      </div>
      <!--      审批弹窗-->
      <el-dialog
          title="审批任务"
          :visible.sync="dialogVisible"
          width="30%"
          :before-close="handleClose">
        <el-form ref="form" :model="form" status-icon :rules="rules" label-width="120px">
          <el-form-item label="审批操作" prop="pass">
            <el-radio-group v-model="form.pass">
              <el-radio label="通过"></el-radio>
              <el-radio label="打回"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="留言" prop="comment">
            <el-input v-model="form.comment" style="width: 80%"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save('form')">确 定</el-button>
        </span>
      </el-dialog>
    </div>
    <Footer/>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import request from "@/utils/request";

export default {
  name: "Task",
  components: {Footer, Header},
  data(){
    return{
      user: "",
      taskId: '',
      taskData: [],
      projectData: [],
      groupData: [],
      dialogVisible: false,
      form: {
        comment:'',
        tid:'',
        pass:'',
      },
      rules:{
        pass:[{required: true, message: '请审批', trigger: 'blur'}],
        comment: [
          {max:30, message: '不能超过30位', trigger: 'blur' }
        ],
      },
    }
  },
  created(){
    this.user = JSON.parse(sessionStorage.getItem("user"));
    this.check();
    this.getParams();
    this.getTask();
  },
  methods:{
    check(){
      if(!this.user)
        this.$router.push('/login')
    },
    getParams(){
      this.taskId = this.$route.query.taskId;
    },
    getTask(){
      request.get("/task/byid",{params:{tid: this.taskId}}).then(res => {
        this.taskData = res.data;
        if(this.taskData.done === 0)
          this.taskData.done = "未完成"
        if(this.taskData.done === 1)
          this.taskData.done = "已完成"
        if(this.taskData.done === 2)
          this.taskData.done = "待审核"
        if(this.taskData.done === 3)
          this.taskData.done = "已终止"
        request.get("/project/byid",{params:{pid: this.taskData.pid}}).then(res => {
          this.projectData = res.data
          request.get("/group/bypid",{params:{pid: this.taskData.pid, uid: this.user.uid}}).then(res => {
            this.groupData = res.data;
          })
        })
      })
    },
    gotoProj(){
      this.$router.push({
        path:'/project',
        query:{
          projectId: this.projectData.pid
        }
      })
    },
    doneTask(){
      this.taskData.done = 1
      request.put("task/done",this.taskData).then(res => {
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "操作成功！"
          })
          this.getTask()
        }
      })
    },
    endTask(){
      this.taskData.done = 3
      request.put("task/end",this.taskData).then(res => {
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "操作成功！"
          })
          this.getTask()
        }
      })
    },
    submitTask(){
      this.taskData.done = 2
      request.put("task/submit",this.taskData).then(res => {
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "操作成功！"
          })
          this.getTask()
        }
      })
    },
    //项目负责人提交审批
    approve(){
      this.dialogVisible = true;
      this.form = {};
    },
    save(formName){
      this.$refs[formName].validate((valid) => {
        if(valid){
          this.form.tid = this.taskId
          if(this.form.pass === "打回")
            this.form.pass = 0
          if(this.form.pass === "通过")
            this.form.pass = 1
          if(this.form.pass === 0){
            this.taskData.done = 0
            request.put("task/undo",this.taskData).then(res => {
              this.getTask()
            })
          }
          if(this.form.pass === 1){
            this.taskData.done = 1
            request.put("task/done",this.taskData).then(res => {
              this.getTask()
            })
          }
          request.post("/comment/add",this.form).then(res => {
            if(res.code === '0'){
              this.$message({
                type: "success",
                message: "添加成功！"
              })
              this.dialogVisible = false
            }else{
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }else{
          alert("请检查！！");
          return false;
        }
      })
    },
    //已提交的文件页
    gotoFileList(){
      this.$router.push({
        path:'/filelist',
        query:{
          taskId: this.taskId
        }
      })
    },
    //审批记录页
    gotoComment(){
      this.$router.push({
        path:'/comment',
        query:{
          taskId: this.taskId
        }
      })
    },
  }
}
</script>

<style scoped>
.link{
  color: limegreen;
  text-decoration: underline;
}
.link:hover{
  cursor: pointer;
}
.a:hover{
  cursor: pointer;
  color: #409eff;
  text-decoration: underline;
}
</style>