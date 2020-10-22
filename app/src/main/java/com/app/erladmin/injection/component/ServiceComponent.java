package com.app.erladmin.injection.component;


import com.app.erladmin.injection.module.DashboardModule;
import com.app.erladmin.injection.module.UserAuthenticationModule;
import com.app.erladmin.injection.scope.UserScope;
import com.app.erladmin.viewModel.DashboardViewModel;
import com.app.erladmin.viewModel.UserAuthenticationViewModel;

import dagger.Component;

@UserScope
@Component(dependencies = NetworkComponent.class
        , modules = {UserAuthenticationModule.class, DashboardModule.class})

public interface ServiceComponent {
    void inject(UserAuthenticationViewModel userAuthenticationViewModel);

    void inject(DashboardViewModel dashboardViewModel);
}
