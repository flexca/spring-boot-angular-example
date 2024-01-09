import { TableWrapperFilterType } from "./table-wrapper-filter.type";
import { formatDate } from "@angular/common";

export class TableWrapperFilterValueModel {

    constructor(
        public id: string,
        public title: string,
        public type: TableWrapperFilterType,
        public value: any) {  
    }

    public toString(): string {
        let data =  this.title + ":";
        if(TableWrapperFilterType.date == this.type) {
            const startDate = this.value['startDate'] as Date;
            if(startDate) {
                data += " from " + formatDate(startDate, 'dd MMM yyyy', 'en-US'); 
            }
            const endDate = this.value['endDate'] as Date;
            if(endDate) {
                data += " to " + formatDate(endDate, 'dd MMM yyyy', 'en-US'); 
            }
        } else {
            data += this.value;
        }
        return data;
    }
}