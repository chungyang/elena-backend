package com.elena.elena.dao;

import com.elena.elena.model.AbstractElenaNode;
import com.elena.elena.util.Units;

import java.io.Closeable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ElevationDao extends Closeable {

    int insert(Collection<? extends AbstractElenaNode> nodes);

    int delete(Set<? extends AbstractElenaNode> nodes);

    int get(Collection<? extends AbstractElenaNode> nodes, Units unit);

    int update(Collection<? extends AbstractElenaNode> nodes);
}
