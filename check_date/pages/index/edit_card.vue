<template>
	<view>
		<view class="creat_part">
			<view class="name_part">
				<view padding-down:20rpx>习惯名称:</view>
				<input class= "input_part"  type="text" :placeholder="input_tap" confirm-type="done" @confirm="get_name" />
			</view>
			<view class="time_part">
				<view class="habit_time">想在什么时间完成它：</view>
				<view class="piker_time">
					<picker @change="picker_change_time" :value="index" :range="array">
						<view class="uni-input">{{array[index]}}</view>
					</picker>
				</view>
			</view>
			<view class="clock_part">
				<view class="clock_checked">{{checked_text}}：</view>
				<switch checked='true' @change="no_clock" />	
			</view>
			<view class="img_part">
				<view class="check_img_part">选取习惯图片</view>
				<view>
					<scroll-view class="scroll_img" scroll-x="true" scroll-left="120" v-for="i in img">
						<view class="show_img">
							<image :src="i" mode="aspectFit" @click="click_img(i)"></image>
						</view>
					</scroll-view>
				</view>
			</view>
		</view>
		<view class="button_part">
			<button class="push_crad" type="primary" size="mini" @click="push_button">修改</button>
		</view>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				
				img_object:[{}],
				uid: 1,
				title:"习惯",
				index: 0,
				array: ['起床', '晨间', '午间', '晚间', '睡前'],
				check_time:7,
				check_flag: true,
				checked_text: '需要提醒',
				img: [
					'../../static/png/1.png',
					'../../static/png/2.png',
					'../../static/png/3.png',
					'../../static/png/4.png',
					'../../static/png/5.png',
					'../../static/png/6.png',
					'../../static/png/7.png',
					'../../static/png/8.png',
					'../../static/png/9.png',
					'../../static/png/10.png',
				],
				img_id: 0,
			}
		},

		onLoad: function(e) {
			this.input_tap=e.title,
			this.check_flag = e.check_flag,
			this.img_id = e.img_id
		},

		methods: {
			/*提醒选择*/
			set_clock() {
				this.checked_text = "不需要提醒",
					this.check_flag = false;
			},
			/*图片点击选择*/
			click_img(e) {
				this.img_object = e.img_object;
			},
			/*获取新标题*/
			get_name: function(event) {
				this.title = event.target.value
			},
			/*修改打卡申请*/
			push_button() {
				uni.request({
					url: 'http://localhost/api/habit/revise',
					method: 'GET',
					data: {
						uid: this.uid,
						title: this.title,
						remind: this.check_flag,
						img: this.img_id,
					},
					success: function(e) {
						console.log(e)
					}
				})
			}
		}
	}
</script>

<style>
	.creat_part {
		flex-direction: column;
		flex-wrap: wrap;
		justify-content: space-around;
		align-content: flex-start;
		flex: auto |[ <'flex-grow'> <'flex-shrink'>? || <'flex-basis'>]
	}

	.show_img {
		padding-left: 20rpx;
		padding-right: 10rpx;
	}
	.input_part{
		
		height: 80rpx;
		border: 2px #4CD964 solid;
	}
	.name_part{
		padding: 20rpx;
		border: 2px red solid;
	}
	.time_part{
		padding: 20rpx;
		border: 2px red solid;
	}
	.clock_part{
		padding: 20rpx;
		border: 2px red solid;
	}
	.img_part{
		padding: 20rpx;
		border: 2px red solid;
	}
</style>
