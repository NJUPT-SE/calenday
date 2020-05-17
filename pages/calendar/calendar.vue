<template>
	<view>
		<!-- 日历st -->
		<view class="light bg-orange solid">
			<uni-calendar ref="calendar" lunar="" @change="change"></uni-calendar>
		</view>
		<!-- 日历ed -->

		<!-- 今日任务st -->

		<view class="padding bg-white">
			
			<view class="">
				<view class="flex solid-bottom justify-end">
					<button class="cu-btn round light bg-blue" @tap="weather">天气</button>
				</view>

				<text class="cuIcon-title text-orange padding-top"></text> 
				<text class="padding-right">打卡任务</text>
				<view class="cu-tag round light bg-orange">{{today}}</view>
				<view class="cu-tag round light bg-orange">{{week}}</view>
			</view>

		
			<view class="cu-list menu">
				<view class="cu-item" v-for="task in tasks" :key="task">
					<view class="content">
						<text class="cuIcon-tagfill text-green  margin-right-xs"></text>
						<text class="text-grey">{{task}}</text>
					</view>
				</view>
			</view>
			
		</view>
		<!-- 今日任务ed -->
		<view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				today: "",
				week: "",
				tasks:["任务1","任务2","任务3"],
				// task: "暂无任务",
			}
		},
		methods: {
			change(e) {
				this.today = e["fulldate"];
				this.week = e["lunar"]["ncWeek"];
				// console.log(e);
				if(this.tasks.length!=2){
					this.tasks.shift();
				}
				else{
					this.tasks=["按时吃饭","完成接口","早点睡觉"];
				}
			},
			
			weather:function(){
				uni.navigateTo({
					url:"./weather",
				})
			}
			
		},
		mounted: function() {
			this.$nextTick(function() {
				// Code that will run only after the
				// entire view has been rendered
				this.today = this.$refs.calendar.$data.nowDate.fullDate;
				this.week = this.$refs.calendar.$data.nowDate.lunar.ncWeek;
			})
		}
	}
</script>

<style>

</style>
