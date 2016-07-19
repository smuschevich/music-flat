import {Component} from '@angular/core';
import {RouteConfig} from '@angular/router-deprecated';
import {HomeComponent} from './home/HomeComponent';
import {LoggedInRouterOutlet} from './LoggedInRouterOutlet';
import {DashboardComponent} from './dashboard/DashboardComponent';

@Component({
	selector: 'music-flat-app',
	template: '<router-outlet></router-outlet>',
	directives: [LoggedInRouterOutlet]
})
@RouteConfig([
	{path: '/home', name: 'Home', component: HomeComponent, useAsDefault: true},
	{path: '/dashboard', name: 'Dashboard', component: DashboardComponent}
])
export class AppComponent {
}