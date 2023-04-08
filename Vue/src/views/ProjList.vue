<template>
  <div>
    <Header/>
    <div style="margin: 10px 10px; height: 630px; background-color: white">
      <div style="padding-top: 10px; color: #606266; font-size: 25px; text-align: center">我的全部项目</div>
      <!--      表格展示-->
      <div style="padding: 20px">
        <el-table
            class="list"
            :data="tableData"
            border
            stripe
            style="width: 100%"
            @row-click="openDetails">
          <el-table-column
              prop="title"
              label="名称"
              width="300">
          </el-table-column>
          <el-table-column
              prop="startdate"
              label="创建时间"
              sortable
              width="300">
          </el-table-column>
          <el-table-column
              prop="ddl"
              label="截止时间"
              sortable
              width="300">
          </el-table-column>
          <el-table-column
              prop="done"
              label="状态"
              :filters="[{ text: '已完成', value: '已完成' }, { text: '未完成', value: '未完成' }, { text: '已终止', value: '已终止' }]"
              :filter-method="filterTag">
          </el-table-column>
        </el-table>
        <!--        分页-->
        <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="currentPage"
            layout="prev, pager, next"
            :total="100"
            style="margin-top: 20px; margin-left: 530px; float: left">
        </el-pagination>
        <div style="float: right; margin-top: 15px">
          <el-button @click="add" size="medium" type="success" style="margin-right: 10px;">新增项目</el-button>
        </div>
      </div>
      <!--      新增弹窗-->
      <el-dialog
          title="新增项目"
          :visible.sync="dialogVisible"
          width="30%"
          :before-close="handleClose">
        <el-form ref="form" :model="form" status-icon :rules="rules" label-width="120px">
          <el-form-item label="项目名称" prop="title">
            <el-input v-model="form.title" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="项目简介" prop="intro">
            <el-input v-model="form.intro" type="textarea" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="团队ID" prop="gid">
            <el-input v-model="form.gid" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="创建时间" prop="startdate">
            <el-date-picker type="date" placeholder="选择日期" v-model="form.startdate" style="width: 80%;"></el-date-picker>
          </el-form-item>
          <el-form-item label="截止时间" prop="ddl">
            <el-date-picker type="date" placeholder="选择日期" v-model="form.ddl" style="width: 80%;"></el-date-picker>
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
  name: "ProjList",
  components: {Footer, Header},
  data(){
    return{
      user: '',
      dialogVisible: false,
      currentPage: 1,
      form: {
        title: '',
        intro: '',
        user: '',
        gid: '',
        startdate: '',
        ddl: ''
      },
      rules: {
        title: [
          {required: true, message: '请输入名称', trigger: 'blur'},
          {max:10, message: '不能超过10位', trigger: 'blur' }
        ],
        intro: [
          {max:30, message: '不能超过30位', trigger: 'blur' }
        ],
        gid: [
          {required: true, message: '请输入团队ID', trigger: 'blur'}
        ],
        startdate: [
          {required: true, message: '请选择日期', trigger: 'blur'}
        ],
        ddl: [
          {required: true, message: '请选择日期', trigger: 'blur'}
        ]
      },
      tableData: []
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user"));
    this.check()
    this.load()
  },
  methods: {
    check(){
      if(!this.user)
        this.$router.push('/login')
    },
    add(){
      this.dialogVisible = true;
      this.form = {};
    },
    save(formName){
      this.$refs[formName].validate((valid) => {
        if(valid){
          this.form.uid = this.user.uid
          console.log(this.form)
          request.post("/project/save",this.form).then(res => {
            if(res.code === '0'){
              this.$message({
                type: "success",
                message: "添加成功！"
              })
              this.load()
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
    load(){
      request.get("/project/load", {params:{pageNum: this.currentPage, pageSize:9, uid: this.user.uid}}).then(res => {
        this.tableData = res.data.records;
        this.tableData.filter((item) => {
          if(item.done === 0)
            item.done = "未完成"
          if(item.done === 1)
            item.done = "已完成"
          if(item.done === 2)
            item.done = "已终止"
        })
      })
    },
    handleCurrentChange(val){
      this.currentPage = val;
      this.load()
    },
    openDetails(row){
      this.$router.push({
        path:'/project',
        query:{
          projectId: row.pid
        }
      })
    },
    filterTag(value, row) {
      return row.done === value;
    }
  }
}
</script>

<style scoped>
.list:hover{
  cursor: pointer;
}
</style>