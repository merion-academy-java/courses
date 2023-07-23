package com.example.courses.course.routes;

import com.example.courses.base.routes.BaseRoutes;

public class CourseRoutes {
    private static final String ROOT = BaseRoutes.API + "/course";
    public static final String CREATE = ROOT;
    public static final String BY_ID = ROOT + "/{id}";
    public static final String EDIT = BY_ID;
    public static final String SEARCH = ROOT;
}
