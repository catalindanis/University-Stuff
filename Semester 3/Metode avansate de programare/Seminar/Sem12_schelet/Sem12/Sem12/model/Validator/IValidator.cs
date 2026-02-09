using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Sem12.Model.Validator
{
    public interface IValidator<E>
    {
        void Validate(E e);
    }
}
