package com.elena.elena.dao;

import java.io.Closeable;
import java.util.List;

public interface ElevationDao{

    boolean insert(List<ElevationData> elevationData);

    boolean delete(List<ElevationData> elevationData);

    List<ElevationData> get(List<String> ids);

    boolean update(List<ElevationData> elevationData);
}
