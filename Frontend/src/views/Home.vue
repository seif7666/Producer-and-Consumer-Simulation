<template>
  <div class="home">
    <Simulator />
  </div>
</template>

<script>
// @ is an alias to /src
import Simulator from "@/components/Simulator.vue";

export default {
  name: "Home",
  components: {
    Simulator
  }
};
</script>
