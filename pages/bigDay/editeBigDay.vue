<template>
	<view class="bg-white">

		<!-- title st -->
		<view class="cu-form-group">
			<view class="text-xl padding-right text-bold text-black ">纪念日命名</view>
			<input placeholder="它的名字是什么呢......？" name="input" @input="titleInput" v-model="title"></input>
		</view>
		<!-- title ed -->

		<!-- date st -->
		<view class="cu-form-group solid">
			<text class="flex text-bold text-black text-xl">日期选择</text>
			<picker mode="date" :date="date" @change="DateChange">
				<view class="picker">
					{{date}}
				</view>
			</picker>
		</view>
		<!-- date ed -->

		<!-- note st -->
		<view class="padding solid">
			<text class="flex text-bold text-xl text-black">随笔</text>
			<textarea class="padding-top" maxlength="-1" @input="noteInput" placeholder="写个小纸条塞入记忆里" v-model="note">
			</textarea>
		</view>
		<!-- note ed -->

		<!-- img st -->
		<view class="padding solid">
			<text class="flex text-bold text-xl text-black">背景颜色 </text>
			<view class="grid col-5 padding-top-sm">
				<view class="margin-tb-sm text-center" v-for="(item,index) in ColorList" :key="index">
					<button class="cu-btn round shadow-blur light" :class="'bg-' + item.name" @tap="selectImg(index)">{{item.title}}
						<view class='cu-tag badge cuIcon-check' :class="'bg-' + item.name" v-if="index == img"></view>
					</button>
				</view>
			</view>

			</textarea>
		</view>
		<!-- img ed -->

		<!-- submit st -->
		<view class="padding text-center">
			<button class="cu-btn bg-orange light shadow-lg" @tap="edite" data-target="Modal">
				<text class="cuIcon-discover padding-right"></text> 回炉重造
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
				ColorList: [{
						title: '嫣红',
						name: 'red',
						color: '#e54d42'
					},
					{
						title: '桔橙',
						name: 'orange',
						color: '#f37b1d'
					},
					{
						title: '明黄',
						name: 'yellow',
						color: '#fbbd08'
					},
					{
						title: '橄榄',
						name: 'olive',
						color: '#8dc63f'
					},
					{
						title: '森绿',
						name: 'green',
						color: '#39b54a'
					},
					{
						title: '天青',
						name: 'cyan',
						color: '#1cbbb4'
					},
					{
						title: '海蓝',
						name: 'blue',
						color: '#0081ff'
					},
					{
						title: '姹紫',
						name: 'purple',
						color: '#6739b6'
					},
					{
						title: '木槿',
						name: 'mauve',
						color: '#9c26b0'
					},
					{
						title: '桃粉',
						name: 'pink',
						color: '#e03997'
					},
				],
				uid: 0,
				id: 0,
				title: '',
				date: '2020-01-01',
				note: '',
				img: 0,
				modalName: null,
				noticeId: 0,
				notice: [
					"最是人间留不住，朱颜辞镜花辞树。",
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
			DateChange: function(e) {
				this.date = e.detail.value
			},
			titleInput: function(e) {
				this.title = e.detail.value
			},
			noteInput: function(e) {
				this.note = e.detail.value
			},
			selectImg: function(index) {
				this.img = index;
			},
			edite: function() {
				const _this = this;
				if (!this.checkEmpty()) {
					console.log("没有名字是不会被记住的哦~");
				} else {
					uni.request({
						method: 'GET',
						data: {
							uid: _this.uid,
							id: _this.id,
							date: _this.date,
							title: _this.title,
							notes: _this.note,
							img: _this.img
						},
						url: 'http://localhost:8080/api/bigDay/revise',
						success: (res) => {
							if (res.data.err == 0) {
								_this.noticeId = Math.floor(Math.random() * 10) % 5;
								console.log(_this.noticeId);
								_this.showModal();
								setTimeout(function() {
									_this.hideModal();
									uni.switchTab({
										url: './bigDay'
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


			initPage: function() {
				const _this = this;
				//获取id
				wx.login({
					success(res) {
						if (res.code) {
							//发起网络请求
							const secret = res.code;
							let uinfo = {};
							wx.getUserInfo({
								success: function(res) {
									uinfo = res.userInfo;
									uni.request({
										method: "GET",
										url: "http://localhost:8080/api/UserManage",
										header: {
											"content-type": "application/json"
										},
										data: {
											code: secret,
											gender: uinfo.gender,
											nickname: uinfo.nickName,
											avaUrl: uinfo.avatarUrl
										},
										success: (res) => {
											_this.uid = res.data.uid;
											_this.getBigDays();
										},
										fail: (res) => {
											// console.log(res);
											console.log('登录失败!');
										}
									})
								}
							});
						} else {
							console.log('登录失败！' + res.errMsg)
						}
					}
				});

			},

			//获取bigdays
			getBigDays: function() {
				const _this = this;
				//根据id获取清单
				// console.log(this.uid);
				uni.request({
					method: 'GET',
					url: 'http://localhost:8080/api/bigDay/view',
					data: {
						uid: this.uid,
					},
					success: (res) => {
						if (res.data.err == 0) {
							console.log(res);
							for (let i = 0; i < res.data.data.length; i++) {
								if (res.data.data[i].id == _this.id) {
									_this.title = res.data.data[i].title;
									_this.date = res.data.data[i].date;
									_this.img = res.data.data[i].img;
									_this.note = res.data.data[i].notes;
								}
							}
						} else {
							console.log(res);
							console.log("Bad request.");
						}
					},
					fail: () => {
						console.log("Request failed.");
					}
				})
			},

		},
		onLoad: function(option) { //option为object类型，会序列化上个页面传递的参数
			this.uid = option.uid;
			this.id = option.id;
			this.initPage();
		},
		mounted: function() {
			this.$nextTick(function() {

				this.initPage();
			})
		},
	}
</script>

<style>
	.cu-form-group .title {
		min-width: calc(4em + 15px);
	}
</style>
