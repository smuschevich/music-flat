import {Component} from '@angular/core';
import {RouteConfig, Router} from '@angular/router-deprecated';
import {HomeComponent} from './home/HomeComponent';
import {LoggedInRouterOutlet} from './LoggedInRouterOutlet';
import {DashboardComponent} from './dashboard/DashboardComponent';
import {Location} from '@angular/common';

@Component({
	selector: 'music-flat-app',
	template: '<router-outlet></router-outlet>',
	directives: [LoggedInRouterOutlet]
})
@RouteConfig([
	{path: '/home', name: 'Home', component: HomeComponent, useAsDefault: true},
	// {regex: '_=_',  name: 'OAuthRedirectUri', serializer: (p) => console.log(p), component: HomeComponent},
	{path: '/dashboard', name: 'Dashboard', component: DashboardComponent}
])
export class AppComponent {
	constructor(private location: Location, private router: Router) {
		this.verifyOauthUrlHash();
	}

	private verifyOauthUrlHash() {
		if (this.location.path(true) === '_=_') {
			this.router.navigateByUrl('/');
		}
	}
}