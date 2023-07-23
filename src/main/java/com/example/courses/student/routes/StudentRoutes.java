package com.example.courses.student.routes;


import com.example.courses.base.routes.BaseRoutes;

public class StudentRoutes {
    private final static String ROOT = BaseRoutes.API + "/student";
    public final static String REGISTRATION = BaseRoutes.NOT_SECURED + "/v1/registration";
    public final static String EDIT = ROOT;
    public final static String BY_ID = ROOT + "/{id}";
    public final static String SEARCH = ROOT;
    public final static String INIT = BaseRoutes.NOT_SECURED + "/init";
}
