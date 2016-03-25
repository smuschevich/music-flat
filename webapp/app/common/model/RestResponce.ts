export class RestResponse<T> {
	constructor(public success: boolean, public message: string, public data: T) {
	}
}