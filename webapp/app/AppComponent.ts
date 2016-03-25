import {Component} from 'angular2/core';
import {RouteConfig} from 'angular2/router';
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