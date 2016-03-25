import {Http, RequestOptionsArgs, Response} from 'angular2/http';
import {Observable} from 'rxjs/Observable';
import {REST_API_URL} from '../../AppConstants';
import {RestResponse} from '../model/RestResponce';

export class BaseRemoteService {
	constructor (private http: Http) {
		// todo: Use official Angular2 CORS support when merged (https://github.com/angular/angular/issues/4231).
		let _build = (<any> http)._backend._browserXHR.build;
		(<any> http)._backend._browserXHR.build = () => {
			let _xhr =  _build();
			_xhr.withCredentials = true;
			return _xhr;
		};
	}

	get(path: string, options?: RequestOptionsArgs): Observable<Response> {
		return this.http.get(this.getUrl(path), options).map(r => this.getRestResponse(r.json()));
	}

	post(path: string, body: any, options?: RequestOptionsArgs): Observable<Response> {
		return this.http.post(this.getUrl(path), JSON.stringify(body), options).map(r => this.getRestResponse(r.json()));
	}

	put(path: string, body: any, options?: RequestOptionsArgs): Observable<Response> {
		return this.http.put(this.getUrl(path), JSON.stringify(body), options).map(r => this.getRestResponse(r.json()));
	}

	public getRestResponse<T>(json: any): RestResponse<T> {
		return new RestResponse<T>(json.success, json.message, json.data);
	}

	private getUrl(path: string): string {
		return REST_API_URL + path;
	}
}