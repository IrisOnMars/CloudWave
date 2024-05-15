<script setup>
import homepage from "./components/homepage.vue";
import temp from "./components/temp.vue"
</script>

<template>

    <div class="main-body"> 
      
      <router-view></router-view>
  
    </div>
  

</template>

<style scoped>


.main-body{
   position:static; 
   
   width: 100%;
  height: 100%;
   padding: 0;
   margin: 0;
   outline: none;
   border: 0;
  }
  

 

</style>
