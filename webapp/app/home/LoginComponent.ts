import {Component} from 'angular2/core';
import {SecurityService} from '../common/service/SecurityService';
import {Credentials} from '../common/model/Credentials';
import {Router} from 'angular2/router';

@Component({
	selector: 'login',
	templateUrl: 'app/home/Login.html',
	styleUrls: ['app/home/Login.css']
})
export class LoginComponent {
	credentials = new Credentials();

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