import {Injectable} from '@angular/core';
import {SecurityRemoteService} from '../remote/SecurityRemoteService';
import {Credentials} from '../model/Credentials';

@Injectable()
export class SecurityService {
	private authorized: boolean = false;

	constructor (private securityRemoteService: SecurityRemoteService) {
	}

	login(credentials: Credentials, callback: (a: boolean) => void) {
		this.securityRemoteService.login(credentials).then(r => {
			this.authorized = r.data;
			this.isAuthorized(a => callback(a));
		});
	}

	isAuthorized(callback: (a: boolean) => void) {
		if (!this.authorized) {
			this.securityRemoteService.isAuthorized().then(r => {
				this.authorized = r.data;
				callback(this.authorized);
			});
		} else {
			callback(this.authorized);
		}
	}

	logout(callback: (l: boolean) => void) {
		this.securityRemoteService.logout().then(r => {
			this.authorized = !r.data;
			callback(this.authorized);
		});
	}
}