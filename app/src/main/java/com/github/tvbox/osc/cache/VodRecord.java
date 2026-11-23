package com.github.tvbox.osc.cache;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * @author pj567
 * @date :2021/1/7
 * @description:
 */
@Entity(tableName = "vodRecord")
public class VodRecord implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "vodId")
    public String vodId;
    @ColumnInfo(name = "updateTime")
    public long updateTime;
    @ColumnInfo(name = "sourceKey")
    public String sourceKey;
    public String dataJson;
    // 续播进度（毫秒）：看到第几分钟。99% 及以上视为看完，不进"继续观看"
    @ColumnInfo(name = "progress")
    public long progress = 0;
    @ColumnInfo(name = "duration")
    public long duration = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}