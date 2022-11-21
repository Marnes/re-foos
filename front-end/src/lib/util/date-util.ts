import moment from 'moment';

export const humanDate = (date: Date): string => {
   const momentDate = moment(date);

   if (momentDate.isBefore(moment().subtract(1, 'months'))) {
       return momentDate.format('MMM Do YYYY, h:mm a');
   }

   return momentDate.calendar();
}
