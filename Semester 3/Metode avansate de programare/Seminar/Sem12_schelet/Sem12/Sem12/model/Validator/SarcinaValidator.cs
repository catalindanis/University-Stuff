using Sem12.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Sem12.Model.Validator
{
    class SarcinaValidator : IValidator<Sarcina>
    {
        public void Validate(Sarcina e)
        {
            bool valid = true;
            if (valid == false)
            {
                throw new ValidationException("Obiectul nu e valid");
            }
        }
    }
}
