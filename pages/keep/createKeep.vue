<template>
	<view class="bg-white">

		<!-- title st -->
		<view class="cu-form-group">
			<view class="text-xl padding-right text-bold text-black ">我要立个Flag</view>
			<input placeholder="是什么呢......？" name="input" @input="titleInput"></input>
		</view>
		<!-- title ed -->

		<!-- date st -->
		<view class="cu-form-group solid">
			<text class="flex text-bold text-black text-xl">时间段选择</text>
			<view class="grid col-3">
				<view class="padding text-center" v-for="(item,index) in timeList" :key="index">
					<button class="cu-btn line-orange round shadow-blur "  @tap="selectTime(index)">{{item.title}}
						<view class='cu-tag badge cuIcon-check bg-orange light' v-if="index == time"></view>
					</button>
				</view>
			</view>
		</view>
		<!-- date ed -->

		<!-- img st -->
		<view class="padding solid">
			<text class="flex text-bold text-xl text-black">图标选择 </text>
			<view class="grid col-5 padding-top-sm">
				<view class="margin-tb-sm text-center" v-for="(item,index) in imgList" :key="index">
					<view class="cu-avatar lg bg-white" :style="[{ backgroundImage:'url(../../static/pic' + index + '.png)'}]" @tap="selectImg(index)">
						<view class='cu-tag badge cuIcon-check bg-orange light' v-if="index == img"></view>
					</view>
				</view>
			</view>

			</textarea>
		</view>
		<!-- img ed -->

		<!-- submit st -->
		<view class="padding text-center">
			<button class="cu-btn bg-orange light shadow-lg" @tap="create" data-target="Modal">
				<text class="cuIcon-selection padding-right"></text> 这个Flag我立下了
			</button>
		</view>
		<!-- submit ed -->

		<!-- notice board st -->
		<view class="cu-modal" :class="modalName=='Modal'?'show':''">
			<view class="cu-dialog">
				<view class="cu-bar bg-white justify-end">
					<view class="content">提醒</view>
					<view class="action" @tap="hideModal">
						<text class="cuIcon-close text-red"></text>
					</view>
				</view>
				<view class="padding-xl">
					{{notice[noticeId]}}
				</view>
			</view>
		</view>
		<!-- notice board ed -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				imgList: [{
						title: '咖啡',
					},
					{
						title: '健身',
					},
					{
						title: '早餐',
					},
					{
						title: '按时吃饭',
					},
					{
						title: '洗澡',
					},
					{
						title: '学习',
					},
					{
						title: '篮球',
					},
					{
						title: '约会',
					},
					{
						title: '旅行',
					},
					{
						title: '跑步',
					},
				],
				timeList: [{
					title: "早晨"
				}, {
					title: "中午"
				}, {
					title: "晚间"
				}],
				uid: 0,
				title: '',
				time: 0,
				img: "0",
				modalName: null,
				noticeId: 0,
				notice: [
					"自己定的计划，哭着也要完成嗷。",
					"轻描淡写的曾经，却是难忘的刻骨铭心。",
					"天空没有痕迹，风雨已在心中。",
					"谁说“日月既往，不可复追”",
					"从前的锁也好看，钥匙精美有样子",
				]
			};
		},
		methods: {
			showModal: function(e) {
				this.modalName = 'Modal';
			},
			hideModal: function(e) {
				this.modalName = null
			},
			titleInput: function(e) {
				this.title = e.detail.value
			},
			selectImg: function(index) {
				this.img = index;
			},
			selectTime: function(index) {
				this.time = index;
			},
			create: function() {
				const _this = this;
				if (!this.checkEmpty()) {
					console.log("没有名字是不会被记住的哦~");
				} else {
					uni.request({
						method: 'GET',
						data: {
							uid: _this.uid,
							remind:1,
							time: _this.time,
							title: _this.title,
							img: _this.img
						},
						url: 'http://localhost:8080/api/habit/build',
						success: (res) => {
							if (res.data.err == 0) {
								_this.noticeId = Math.floor(Math.random() * 10) % 5;
								console.log(_this.noticeId);
								_this.showModal();
								setTimeout(function() {
									_this.hideModal();
									uni.switchTab({
										url: './keep'
									})
								}, 1500);
							} else {
								console.log("Create failed.");
							}
						}
					})
				}

			},
			checkEmpty: function() {
				if (this.title === '') {
					return false;
				} else {
					return true;
				}
			},

		},
		onLoad: function(option) { //option为object类型，会序列化上个页面传递的参数
			console.log(option.uid); //打印出上个页面传递的参数。
			console.log(option.id); //打印出上个页面传递的参数。
		}
	}
</script>

<style>
	.cu-form-group .title {
		min-width: calc(4em + 15px);
	}
</style>
