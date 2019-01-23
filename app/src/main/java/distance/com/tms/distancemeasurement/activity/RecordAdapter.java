package distance.com.tms.distancemeasurement.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import distance.com.tms.distancemeasurement.POJO_Class.RecordDto;
import distance.com.tms.distancemeasurement.R;

public class RecordAdapter extends ArrayAdapter {
    private Context context;
    private List<RecordDto> recordDtos;
    private LayoutInflater inflater;

    public RecordAdapter(Context context, List<RecordDto> recordDtos) {
        super(context,0,recordDtos);
        this.context=context;
        this.recordDtos=recordDtos;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        ViewHolder holder=new ViewHolder();
        if(v==null){

            v=inflater.inflate(R.layout.record_item_layout,null);
            v.setTag(holder);

        }else{
           v.getTag();
        }
        holder.no=v.findViewById(R.id.no);
        holder.name=v.findViewById(R.id.name);
        holder.steps=v.findViewById(R.id.steps);
        holder.distance=v.findViewById(R.id.distance);
        holder.calorieBurn=v.findViewById(R.id.calorie_burn);

        holder.no.setText(String.valueOf(position+1));
        holder.name.setText(recordDtos.get(position).getFullName());
        holder.steps.setText(recordDtos.get(position).getSteps());
        holder.distance.setText(recordDtos.get(position).getDistance());
        holder.calorieBurn.setText(recordDtos.get(position).getCaloriesBurn());

        return v;
    }

    public static class ViewHolder{
       TextView no,name,steps,distance,calorieBurn;


    }
}
