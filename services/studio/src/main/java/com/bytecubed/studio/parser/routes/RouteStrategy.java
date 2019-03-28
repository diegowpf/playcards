package com.bytecubed.studio.parser.routes;

import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.studio.parser.ShapeToEntityRegistry;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.util.List;

public interface RouteStrategy {
    List<CustomRoute> buildRoutes(XSLFSlide slide, ShapeToEntityRegistry entityRegistry);
}
