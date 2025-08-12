package com.pilltracker.info

import com.pilltracker.entity.ChildEntity

class StatisticListInfo(
    val child: ChildEntity,
    val list: List<StatisticByOneInfo>
)