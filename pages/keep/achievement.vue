<template>
	<view>
		<!-- 展示所有打卡 -->
		<view class="cu-list menu">
			<view class="cu-item" v-for="item in testData" :key="item.id">
				<view class="content col-1 flex solid-bottom">
					<view class="cu-avatar lg bg-white" :style="[{ backgroundImage:'url(../../static/pic' + item.img + '.png)'}]"></view>
					<text class="text-grey padding text-xl">{{item.title}}</text>
				</view>
				<text class="text-black text-xl padding" style="margin-left: 20%;">{{item.number}}天</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				testData: [{
						id: 1,
						title: "打卡内容A",
						img: "2",
						time: 1,
						number: "10",
					},
					{
						id: 2,
						title: "打卡内容B",
						img: "1",
						time: 1,
						number: "3",
					},
					{
						id: 3,
						title: "打卡内容C",
						img: "3",
						time: 2,
						number: "1",
					},
					{
						id: 4,
						title: "打卡内容D",
						img: "4",
						time: 3,
						number: "5",
					},
					{
						id: 5,
						title: "打卡内容E",
						img: "5",
						time: 3,
						number: "31",
					},
					{
						id: 7,
						title: "打卡内容F",
						img: "7",
						time: 2,
						number: "0",
					},
					{
						id: 8,
						title: "打卡内容G",
						img: "8",
						time: 2,
						number: "3",
					},
					{
						id: 9,
						title: "打卡内容H",
						img: "9",
						time: 2,
						number: "9",
					},
					{
						id: 10,
						title: "打卡内容I",
						img: "10",
						time: 2,
						number: "31",
					},
					{
						id: 6,
						title: "打卡内容K",
						img: "6",
						time: 3,
						number: "100",
					},
				],
			}
		},
		methods: {

		}
	}
</script>

<style>

</style>
