import moment from 'moment';

export const humanDate = (date: Date): string => {
   const momentDate = moment(date);

   if (momentDate.isBefore(moment().subtract(6, 'days'))) {
       return momentDate.format('MMM Do YYYY');
   }

   return momentDate.calendar();
}

export const humanDateWithTime = (date: Date): string => {
    const momentDate = moment(date);

    if (momentDate.isBefore(moment().subtract(6, 'days'))) {
        return momentDate.format('MMM Do YYYY, h:mm a');
    }

    return momentDate.calendar();
}

export const isoDate = (date: Date): string => {
    return moment(date).format('Do MMM YYYY');
}
