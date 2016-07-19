import {Component} from '@angular/core';
import {SecurityService} from '../common/service/SecurityService';
import {Router} from '@angular/router-deprecated';

@Component({
	selector: 'dashboard',
	template: '<div>Dashboard coming soon...</div><div><a href="#" (click)="logout()">Logout</a></div>'
})
export class DashboardComponent {
	constructor(private securityService: SecurityService, private router: Router) {
	}

	logout() {
		this.securityService.logout(a => {
			if (!a) {
				this.router.navigateByUrl('/home');
			}
		});
	}
}