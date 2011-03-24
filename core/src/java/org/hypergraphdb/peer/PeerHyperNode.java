package org.hypergraphdb.peer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.hypergraphdb.HGException;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HyperNode;
import org.hypergraphdb.IncidenceSet;
import org.hypergraphdb.peer.cact.AddAtom;
import org.hypergraphdb.peer.cact.DefineAtom;
import org.hypergraphdb.peer.cact.GetAtom;
import org.hypergraphdb.peer.cact.GetAtomType;
import org.hypergraphdb.peer.cact.GetIncidenceSet;
import org.hypergraphdb.peer.cact.QueryCount;
import org.hypergraphdb.peer.cact.RemoteQueryExecution;
import org.hypergraphdb.peer.cact.RemoveAtom;
import org.hypergraphdb.peer.cact.ReplaceAtom;
import org.hypergraphdb.peer.cact.RunRemoteQuery;
import org.hypergraphdb.peer.workflow.ActivityResult;
import org.hypergraphdb.query.HGQueryCondition;
import org.hypergraphdb.util.HGUtils;
import org.hypergraphdb.util.Pair;

public class PeerHyperNode implements HyperNode
{
    private HyperGraphPeer thisPeer;
    private HGPeerIdentity other;
    
    private void maybeThrow(ActivityResult R)
    {
        Throwable t = R.getException();
        if (t == null)
            return;
        else
            HGUtils.throwRuntimeException(t);
    }
    
    public PeerHyperNode(HyperGraphPeer thisPeer, HGPeerIdentity other)
    {
        this.thisPeer = thisPeer;
        this.other = other;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(HGHandle handle)
    {
        GetAtom A = new GetAtom(thisPeer, handle, other);
        thisPeer.getActivityManager().initiateActivity(A);
        try
        {
            ActivityResult R = A.getFuture().get();
            maybeThrow(R);
        }
        catch (Exception e)
        {
            throw new HGException(e);
        }
        return (T)A.getOneAtom();
    }

    public HGHandle add(Object atom, HGHandle type, int flags)
    {
        final AddAtom A = new AddAtom(thisPeer, atom, type, other);
        thisPeer.getGraph().getTransactionManager().transact(new Callable<Object>(){
            public Object call() {
                thisPeer.getActivityManager().initiateActivity(A);
                return null;
            }
        });
        try
        {
            ActivityResult R = A.getFuture().get();
            maybeThrow(R);
        }
        catch (Exception e)
        {
            throw new HGException(e);
        }
        return A.getAtomHandle();
    }

    public void define(HGHandle handle, HGHandle type,
                       Object instance, int flags)
    {
        DefineAtom A = new DefineAtom(thisPeer, handle, instance, type, other);
        thisPeer.getActivityManager().initiateActivity(A);
        try
        {
            ActivityResult R = A.getFuture().get();
            maybeThrow(R);
        }
        catch (Exception e)
        {
            throw new HGException(e);
        }        
    }

    public boolean remove(HGHandle handle)
    {
        RemoveAtom A = new RemoveAtom(thisPeer, handle, other);
        thisPeer.getActivityManager().initiateActivity(A);
        try
        {
            ActivityResult R = A.getFuture().get();
            maybeThrow(R);
        }
        catch (Exception e)
        {
            throw new HGException(e);
        }                
        return A.getRemoved().get(handle);
    }

    public boolean replace(HGHandle handle, Object newValue, HGHandle newType)
    {
        ReplaceAtom A = new ReplaceAtom(thisPeer, handle, newValue, newType, other);
        thisPeer.getActivityManager().initiateActivity(A);
        try
        {
            ActivityResult R = A.getFuture().get();
            maybeThrow(R);
        }
        catch (Exception e)
        {
            throw new HGException(e);
        }                
        return A.getReplaced();
    }

    public HGHandle getType(HGHandle handle)
    {
        GetAtomType A = new GetAtomType(thisPeer, handle, other);
        thisPeer.getActivityManager().initiateActivity(A);
        try
        {
            ActivityResult R = A.getFuture().get();
            maybeThrow(R);
        }
        catch (Exception e)
        {
            throw new HGException(e);
        }           
        return A.getTypeHandle();
    }

    public IncidenceSet getIncidenceSet(HGHandle handle)
    {
        GetIncidenceSet A = new GetIncidenceSet(thisPeer, handle, other);
        thisPeer.getActivityManager().initiateActivity(A);
        try
        {
            ActivityResult R = A.getFuture().get();
            maybeThrow(R);
        }
        catch (Exception e)
        {
            throw new HGException(e);
        }           
        return A.getIncidenceSet();
    }

    public <T> HGSearchResult<T> find(HGQueryCondition condition)
    {
        RemoteQueryExecution<T> A = new RemoteQueryExecution<T>(thisPeer, condition, other);
        thisPeer.getActivityManager().initiateActivity(A);
        try
        {
            A.getState().getFuture(RemoteQueryExecution.ResultSetOpen).get();
        }
        catch (Exception e)
        {
            throw new HGException(e);
        }           
        return A.getSearchResult();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> List<T> getAll(HGQueryCondition condition)
    {
        RunRemoteQuery A = new RunRemoteQuery(thisPeer, condition, true, other);
        thisPeer.getActivityManager().initiateActivity(A);
        try
        {
            ActivityResult R = A.getFuture().get();
            maybeThrow(R);
        }
        catch (Exception e)
        {
            throw new HGException(e);
        }           
        ArrayList<T> L = new ArrayList<T>();
        for (Pair<HGHandle, T> p : (List<Pair<HGHandle, T>>)(List)A.getResult())
        {
            L.add((T)p.getSecond());
        }            
        return L;        
    }
    
    public List<HGHandle> findAll(HGQueryCondition condition)
    {
        RunRemoteQuery A = new RunRemoteQuery(thisPeer, condition, false, other);
        thisPeer.getActivityManager().initiateActivity(A);
        try
        {
            ActivityResult R = A.getFuture().get();
            maybeThrow(R);
        }
        catch (Exception e)
        {
            throw new HGException(e);
        }           
        return A.getResult();
    }
    
    public long count(HGQueryCondition condition)
    {
        QueryCount A = new QueryCount(thisPeer, condition, other);
        thisPeer.getActivityManager().initiateActivity(A);
        try
        {
            ActivityResult R = A.getFuture().get();
            maybeThrow(R);
        }
        catch (Exception e)
        {
            throw new HGException(e);
        }           
        return A.getResult();        
    }
}