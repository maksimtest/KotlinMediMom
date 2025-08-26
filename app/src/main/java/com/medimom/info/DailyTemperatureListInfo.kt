package com.medimom.info

import com.medimom.entity.ChildEntity


class DailyTemperatureListInfo(
    val child: ChildEntity,
    val list: List<DailyTemperatureByDayInfo>
)