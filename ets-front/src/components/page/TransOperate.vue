<template>

  <div id="myChart" :style="{width: '1400px', height: '400px'}"></div>

</template>

<script>
export default {
  name: "ChartsView",
  components: {
  },
  data(){
    return{
      data:[],
      title:[],
      array:[]
    }
  },

  mounted(){
    this.init();
  },
  created(){

  },
  methods: {

    init(){
      /*分成两个集合*/
      this.data = [];
      this.title = [];
      for(var i =0;i<this.array.length; i++){
        this.data.push(this.array[i].count)
        this.title.push(this.array[i].title)
      }

      this.drawLine();
    },
    drawLine(){
      // 基于准备好的dom，初始化echarts实例

      let myChart = this.$echarts.init(document.getElementById('myChart'))
      // 绘制图表
      myChart.setOption({
        show:true,
        title:{
          shadowColor:'blue'
        },
        animation: true,
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.title
        },
        yAxis: {
          type: 'value'
        },
        textStyle:{
          color:"#FFF",
        },

        areaStyle:{
        },
        series: [{
          data: this.data,
          type: 'line',
          areaStyle: {},
          // itemStyle: {
          //   color:"#4480C4"
          // }
          symbol:'none',  //这句就是去掉点的
          smooth:true,  //这句就是让曲线变平滑的
        }],

        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            {
              offset: 0, color: 'blue' // 0% 处的颜色
            },
            {
              offset: 0.5, color: '#84C1FF' // 100% 处的颜色
            },
            {
              offset: 0.6, color: '#D2E9FF' // 100% 处的颜色
            },
            {
              offset: 1, color: '#84C1FF' // 100% 处的颜色
            },
          ],
          globalCoord: false // 缺省为 false
        }

      },true);
    },
  }
}
</script>



<style scoped>
#myChart{
  padding-top: 80px;
  color: white;
}
</style>