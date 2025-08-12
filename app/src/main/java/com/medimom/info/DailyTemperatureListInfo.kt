package com.pilltracker.info

import com.pilltracker.entity.ChildEntity


class DailyTemperatureListInfo(
    val child: ChildEntity,
    val list: List<DailyTemperatureByDayInfo>
)