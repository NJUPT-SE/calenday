<template>
	<view class="bg-white">

		<!-- title st -->
		<view class="cu-form-group">
			<view class="text-xl padding-right text-bold text-black ">纪念日命名</view>
			<input placeholder="它的名字是什么呢......？" name="input" @input="titleInput"></input>
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
			<text class="flex text-bold text-xl text-black">随笔 </text>
			<textarea class="padding-top" maxlength="-1" @input="noteInput" placeholder="写个小纸条塞入记忆里">
			</textarea>
		</view>
		<!-- note ed -->

		<!-- submit st -->
		<view class="padding">
			<button class="cu-btn bg-orange light shadow-lg" @tap="create">加入回忆清单</button>
		</view>
		<!-- submit ed -->

		<!-- notice board st -->
		<!-- notice board ed -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: '',
				date: '2020-01-01',
				note: '',
			};
		},
		methods: {
			DateChange: function(e) {
				this.date = e.detail.value
			},
			titleInput: function(e) {
				this.title = e.detail.value
			},
			noteInput: function(e) {
				this.note = e.detail.value
			},
			create: function() {
				if (!this.checkEmpty()){
					console.log("没有名字是不会被记住的哦~");
				}
				else{
					console.log(this.title);
					console.log(this.date);
					console.log(this.note);
				}

			},
			checkEmpty: function(){
				if (this.title===''){
					return false;
				}
				else{
					return true;
				}
			}
		}
	}
</script>

<style>
	.cu-form-group .title {
		min-width: calc(4em + 15px);
	}
</style>
