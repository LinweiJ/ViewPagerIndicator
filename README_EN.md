##  ViewPagerIndicator

 A simple, cool, customizable  ViewPagerIndicator，provides five indicator type，custom color,size,type... in layout XML ，in JAVA code  just  two lines of code  to show cool indicator  for  viewpager , it also good for  viewpager  as carousel .

[![](https://jitpack.io/v/LinweiJ/ViewPagerIndicator.svg)](https://jitpack.io/#LinweiJ/ViewPagerIndicator)

![ViewPagerIndicator.gif](https://github.com/LinweiJ/ViewPagerIndicator/blob/master/screen_shot/ViewPagerIndicator.gif)

## Document

- [English](https://github.com/LinweiJ/ViewPagerIndicator/blob/master/README_EN.md)
- [中文](https://github.com/LinweiJ/ViewPagerIndicator/blob/master/README.md)

## Gradle

 Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

Add  the dependency  in your module build.gradle :

```
dependencies {
	        compile 'com.github.LinweiJ:ViewPagerIndicator:0.1.0'
	}
```



## Usage

1.add **ViewPagerIndicator** to your layout xml

```
<com.lwj.widget.viewpagerindicator.ViewPagerIndicator
				android:id="@+id/indicator_line"
				android:layout_width="match_parent"
				android:layout_height="50dp"
				android:background="#efefef"
				app:default_color="#cdcdcd"
				app:distance="800dp"
				app:distanceType="BY_LAYOUT"
				app:indicatorType="LINE"
				app:length="24dp"
				app:radius="8dp"
				app:selected_color="#FF23EEF5"
			/>
```

Properties:

- `app:vpi_selected_color`  
- `app:vpi_default_color`   (if CIRCLE_LINE(indicatorType)  default_color is indicator color ,selected_color not work)
- `app:vpi_radius`  (if CIRCLE_LINE(indicatorType)  radius is indicator height)
- `app:vpi_length`   (work with CIRCLE_LINE(indicatorType))
- `app:vpi_distance`    (work with BY_DISTANCE (distanceType))
- `app:vpi_num`
- `app:vpi_indicatorType` (LINE;  CIRCLE; CIRCLE_LINE; BEZIER ;SPRING)
- `app:vpi_distanceType` (BY_RADIUS  ; BY_DISTANCE ; BY_LAYOUT )
- `app:vpi_animation`(default true)

2.find **ViewPagerIndicator** through code, work with ViewPager

```java
   mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.viewPagerIndicator);
   
   //if viewpager's pages  is limited , exact, fixed , not carousel
   mViewPagerIndicator.setViewPager(mViewpager);

   //if viewpager's carousel ,  ex: allNum=100000  realNum=6 
   //you must give  num = realNum ,ViewPagerIndicator need it to work 
   mViewPagerIndicator.setViewPager(mViewpager,num);
   
   //show ViewPagerIndicator in BannerView
   //more detail see https://github.com/LinweiJ/BannerView
   //see demo/BannerViewActivity
   // if mBannerView.setCircle(true);
   mIndicatorDefault.setViewPager(mBannerView.getViewPager(),true);
   // if mBannerView.setCircle(false);
   mIndicatorDefault.setViewPager(mBannerView.getViewPager(),false);
```

3.more usage  

 *For a working implementation of this project see the app/ folder.*

## License

```
Copyright 2017 LinWeiJia

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## More

Why not give a star ? (>_@)
