package com.example.newtest1.Test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Rational;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newtest1.R;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Random;

@Aspect
public class TraceAspect {
    StringBuffer sb;
    int salt;
    {
        salt=new Random(System.currentTimeMillis()).nextInt(999);
        sb=new StringBuffer();
        sb.append(String.format("%03d",salt));
    }
    Activity ctx;
    @Pointcut("execution(* *.activity.*.onCreate(android.os.Bundle))")
    public void onCreate1stAct(){}
    @Pointcut("execution(void *.onClick1(..))")
    public void onButtonClick() {}


    @Before("onCreate1stAct()")
    public void test1() {
        sb.append(",onCreate");
    }


    @Before("onButtonClick() && args(view)")
    public void test3(View view) {
      String text = ((TextView) view).getText().toString();
      ctx=(Activity) view.getContext();
      sb.append(","+text);
    }

    @Around("onButtonClick()")
    public Object test3_(ProceedingJoinPoint joinPoint) throws Throwable {
        TextView tv=ctx.findViewById(R.id.tv);
        sb.append(","+tv.getText().toString());
        Object result = joinPoint.proceed();
        sb.append(","+tv.getText().toString());
        printResult();
        return result;
    }

    public void printResult(){
//        Log.e("RESULT",sb.toString());
        final Toast toast = Toast.makeText(ctx, ""+sb.toString().hashCode()+""+salt , Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(0xFFBBBBBB);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(Color.RED);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,36);
        new CountDownTimer(60000, 1000)
        {
            public void onTick(long millisUntilFinished) {toast.show();}
            public void onFinish() {toast.show();}
        }.start();


    }






}