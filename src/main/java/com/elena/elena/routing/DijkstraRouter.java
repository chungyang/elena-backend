package com.elena.elena.routing;

import com.elena.elena.model.AbstractElenaNode;

public class DijkstraRouter extends AstarRouter {

	@Override
	protected float getFscore(AbstractElenaNode origin, AbstractElenaNode destination) {
		return this.gScores.get(origin);
	}
}