import {Component} from '@angular/core';
import {SecurityService} from '../common/service/SecurityService';
import {Credentials} from '../common/model/Credentials';
import {Router} from '@angular/router-deprecated';
import {REST_API_URL} from '../AppConstants';

@Component({
	selector: 'login',
	templateUrl: 'app/home/Login.html',
	styleUrls: ['app/home/Login.css']
})
export class LoginComponent {
	credentials = new Credentials();
	facebookLoginUrl = REST_API_URL + '/security/login/facebook';
	gitHubLoginUrl = REST_API_URL + '/security/login/github';

	constructor (private securityService: SecurityService, private router: Router) {
	}

	onSubmit() {
		this.securityService.login(this.credentials, a => {
			if (a) {
				this.router.navigateByUrl('/dashboard');
			}
		});
	}
}