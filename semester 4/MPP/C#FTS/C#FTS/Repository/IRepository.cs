using C_FTS.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace C_FTS.Repository
{
    public interface IRepository <TId, T> where T : Entity<TId>
    {
        T? Save(T entity);
        T? Update(T entity);
        void Delete(T entity);
        T? Find(TId id);
        List<T> FindAll();
    }
}
