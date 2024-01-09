export class SearchResponse<T> {

    constructor(public items: T[], public nextPage: boolean, public limit: number, public offset: number) {  
    }
  
}
