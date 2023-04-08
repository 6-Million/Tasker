<template>
  <div>
    <Header/>
    <div style="height: 5px"></div>
    <SubHeader/>
    <div style="height: 10px"></div>
    <div style="float: left">
      <HomeTask/>
      <div style="height: 10px"/>
      <HomeTeam/>
    </div>
    <div style="float: right">
      <HomeProj/>
    </div>
    <Footer/>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import SubHeader from "@/components/Home/SubHeader.vue";
import HomeTask from "@/components/Home/HomeTask.vue";
import Footer from "@/components/Footer.vue";
import HomeProj from "@/components/Home/HomeProj.vue";
import HomeTeam from "@/components/Home/HomeTeam.vue";

export default {
  name: 'HomeView',
  components: {
    HomeTeam,
    HomeProj,
    Footer,
    HomeTask,
    SubHeader,
    Header
  },
  data(){
    return{
      user: '',
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user"));
    this.check()
  },
  methods: {
    check(){
      if(!this.user)
        this.$router.push('/login')
    }
  }
}
</script>
