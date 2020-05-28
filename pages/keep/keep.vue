<template>
	<view>
		<view class="bg-white padding">
			<text class="text-black text-xl text-bold">晨间</text>
			<view class="bg-white padding">
				<view class="grid col-5 grid-square">
					<view class="bg-img" v-for="item in testData" v-if="item.time==1" :key="item.id" :style="[{ backgroundImage:'url(../../static/pic' + item.img + '.png)'}]"
					 @longpress="clockIn(item.id,item.title)"></view>
				</view>
			</view>
			<text class="text-black text-xl text-bold">午间</text>
			<view class="bg-white padding">
				<view class="grid col-5 grid-square">
					<view class="bg-img" v-for="item in testData" v-if="item.time==2" :key="item.id" :style="[{ backgroundImage:'url(../../static/pic' + item.img + '.png)' } ]"
					 @longpress="clockIn(item.id,item.title)">
					</view>
				</view>
			</view>
			<text class="text-black text-xl text-bold">晚间</text>
			<view class="bg-white padding">
				<view class="grid col-5 grid-square">
					<view class="bg-img" v-for="item in testData" v-if="item.time==3" :key="item.id" :style="[{ backgroundImage:'url(../../static/pic' + item.img + '.png)' }]"
					 @longpress="clockIn(item.id,item.title)">
					</view>
				</view>
			</view>
		</view>
		<view class="cu-modal" :class="modalName=='DialogModal'?'show':''">
			<view class="cu-dialog">
				<view class="cu-bar bg-white justify-end">
					<view class="content text-red">打卡</view>
					<view class="action" @tap="hideModal">
						<text class="cuIcon-close text-red"></text>
					</view>
				</view>
				<view class="padding-xl">
					{{note}}
				</view>
				<view class="cu-bar bg-white col-2">
					<view class="padding">
						<button class="cu-btn bg-green " @tap="comfirm">确认打卡</button>
					</view>
					<view class="padding">
						<button class="cu-btn bg-red margin-left" @tap="hideModal">放弃任务</button>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				modalName: null,
				testData: [{
						id: 1,
						title: "打卡内容A",
						img: "2",
						time: 1,
					},
					{
						id: 2,
						title: "打卡内容B",
						img: "1",
						time: 1,
					},
					{
						id: 3,
						title: "打卡内容C",
						img: "3",
						time: 2,
					},
					{
						id: 4,
						title: "打卡内容D",
						img: "4",
						time: 3,
					},
					{
						id: 5,
						title: "打卡内容E",
						img: "5",
						time: 3,
					},
					{
						id: 7,
						title: "打卡内容F",
						img: "7",
						time: 2,
					},
					{
						id: 8,
						title: "打卡内容G",
						img: "8",
						time: 2,
					},
					{
						id: 9,
						title: "打卡内容H",
						img: "9",
						time: 2,
					},
					{
						id: 10,
						title: "打卡内容I",
						img: "10",
						time: 2,
					},
					{
						id: 6,
						title: "打卡内容K",
						img: "6",
						time: 3,
					},
				],
				note: "",
			}
		},
		methods: {
			showModal: function(e) {
				this.modalName = "DialogModal";
			},

			hideModal: function(e) {
				this.modalName = null
			},

			clockIn: function(eventId, eventTitle) {
				this.showModal();
				this.note = '打卡' + eventTitle + ' 【传参id：' + eventId;
			}
		}
	}
</script>

<style>

</style>
