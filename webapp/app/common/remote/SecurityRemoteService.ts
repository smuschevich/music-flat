import {Injectable} from '@angular/core';
import {BaseRemoteService} from './BaseRemoteService';
import {Http, URLSearchParams} from '@angular/http';
import {Credentials} from '../model/Credentials';
import 'rxjs/Rx';

@Injectable()
export class SecurityRemoteService extends BaseRemoteService {
	constructor (private http: Http) {
		super(http);
	}

	login(credentials: Credentials): Promise {
		let search = new URLSearchParams();
		search.append('email', credentials.email);
		search.append('password', credentials.password);
		return this.post('/security/login', {}, {search: search}).toPromise();
	}

	isAuthorized(): Promise {
		return this.get('/security/authorized').toPromise();
	}

	logout(): Promise {
		return this.get('/security/logout').toPromise();
	}
}